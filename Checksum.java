/*
 * Jonathan Hamm
 * Checksum
 * 
 */

import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

public class Checksum
{
/*
	Frame format, where n is the number of bytes in the frame:
	___________________________________________________________
	byte indices | Description
	0-1 | Header: size of frame in bytes (2 bytes)
	2-13 | Header: reserved for future purpose
	14-(n-3) | Data: datagram
	(n-2)-(n-1) | Trailer: checksum value (trailer = 2 bytes)
	Header consists of 14 bytes.
	Trailer consists of 2 bytes. (the checksum)
	Datagram consists of m bytes.
	The size of the frame is inFrameSize = n = 14 + 2 + m
*/

	static byte[] test = {0, 0, 1, 2 ,3 ,4 ,5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, (byte)235, 00, 0};
	static byte[] test2 = {0,0,'F', 'o', 'r', 'o', 'u', 'z', 'a', 'n', 0, 0};

	private byte [] inFrame; // shallow copy of the input frame.
	private int inFrameSize; // total bytes in inFrame includes header + trailer

	public Checksum(byte[] frame, int size)
	{	 
		// Make a SHALLOW copy of the input frame.
		this.inFrame = frame;
		// Store the frame size in the first two bytes of the input frame. (Assuming big endian)
		frame[0] = (byte)(size >> 8);
		frame[1] = (byte)(size);
	}

	public int getFrameSize()
	{
		// return the frame size = n = 14 + 2 + m bytes of the datagram.
		return inFrameSize;
	}

	public void setFrame(byte[] frame)
	{
		// make a SHALLOW copy of the input frame.
		this.inFrame = frame;
	}

	public void insertChecksum(int chksm)
	{
		//Insert the checksum value into the last two bytes of the input frame. (Assuming big endian)
		this.inFrame[this.inFrameSize-2] = (byte)(chksm >> 8);
		this.inFrame[this.inFrameSize-1] = (byte)chksm;
	}

	public int checksum1()
	{
		//calculate and return the first checksum
		int sum = 0, wrap;
		DataInputStream data = makeStream(this.inFrame, true);

		while(true) {
			try {
				sum += data.readUnsignedShort();
			}
			catch(EOFException eof) {
				wrap = (sum & 0xffff0000) >> 16;
				sum += wrap;
				try {
					data.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			catch(IOException io) {
				System.err.print("IO Error");
			}
		}
		return (~sum & 0x0000ffff);
	}

	public int checksum2()
	{
		//calculate and return the second checksum
		int f = 0, g = 0;
		short word = 0;
		DataInputStream data = makeStream(this.inFrame, false);

		while(true) {
			try {
				f = (f + data.readUnsignedByte()) % 255;
				g = (g + f) % 255;
			}
			catch(EOFException eof) {
				word = (short)((g << 8) | f);
				try {
					data.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			catch(IOException io) {
				System.err.print("IO Error");
			}
		}
		return (~word & 0x0000ffff);

	}

	public int checksum3()
	{
		// calculate and return the third checksum
		int f = 0, g = 0, k;
		short psum;
		DataInputStream data = makeStream(this.inFrame, false);
		
		while(true) {
			try {
				f = (f + data.readUnsignedByte());
				k = f >> 8;
				f %= 255;
				g = (g + f + k) % 255;
				
			} catch (EOFException eof) {
				psum = (short)(g << 8);
				psum |= f;
				try {
					data.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			} catch (IOException io) {
				System.err.print("IO Error");
			}
		}
		return (~psum & 0x0000ffff);
	}

	/*
	 * Converts frame into an input stream. This is mainly only needed to ease the 
	 * task of converting two bytes to an unsigned short integer. This will conditionally
	 * pad the data with a nul byte if the 'pad' parameter is set to true and if the size in 
	 * bytes is odd. 
	 */
	private static DataInputStream makeStream(byte[] data, boolean pad)
	{
		if((data.length % 2) != 0 && pad) {
			int i;
			byte[] data2 = new byte[data.length+1];

			for(i = 0; i < data.length; i++)
				data2[i] = data[i];
			data2[i] = 0;
			data = data2;
		}
		return new DataInputStream(new ByteArrayInputStream(data));
	}
	
	public static void main(String[] args) 
	{
		Checksum c = new Checksum(test, test.length);
		System.out.printf("0x%02x", c.checksum2());
	}
}
