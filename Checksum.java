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

 private byte [] inFrame; // shallow copy of the input frame.

 private int inFrameSize; // total bytes in inFrame includes header + trailer

 public Checksum( byte [] frame, int size )

 {

 // Make a SHALLOW copy of the input frame.

 // Store the frame size in the first two bytes of the input frame.

 }

 public int getFrameSize()

 {

 // return the frame size = n = 14 + 2 + m bytes of the datagram.

 }

 public void setFrame( byte [] frame )

 {

 // make a SHALLOW copy of the input frame.

 }

 public void insertChecksum( int chksm )

 {

 // Insert the checksum value into the last two bytes of the input frame.

 }

 public int checksum1()

 {

 // calculate and return the first checksum

 }

 public int checksum2()

 {

 // calculate and return the second checksum

 }

 public int checksum3()

 {

 // calculate and return the third checksum

 }

}

/*
public class Checksum {
	static byte[] test = {1, 2 ,3 ,4 ,5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
	static byte[] test2 = {'F', 'o', 'r', 'o', 'u', 'z', 'a', 'n'};

	private byte[] inFrame;
	private int inFrameSize;
		
	/*	private byte[] frame;
	
	public Checksum() 
	{
		this.frame = null;
	}
	
	public Checksum(byte[] frame)
	{
		this.frame = frame;
	}
	
	
	public int getFrameSize() 
	{
		return 0;
	}
	
	private static DataInputStream makeStream(byte[] data)
	{
		if((data.length % 2) != 0) {
			int i;
			byte[] data2 = new byte[data.length+1];
			
			for(i = 0; i < data.length; i++)
				data2[i] = data[i];
			data2[i] = 0;
			data = data2;
		}
			
		return new DataInputStream(new ByteArrayInputStream(data));
	}
	
	public static short checksum1(byte[] frame) 
	{
		int sum = 0, wrap;
		boolean done = false;
		DataInputStream data = makeStream(frame);
		
		while(!done) {
			try {
				sum += data.readUnsignedShort();
			}
			catch(EOFException eof) {
				done = true;
				wrap = (sum & 0xffff0000) >> 16;
				sum += wrap;
				try {
					data.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			catch(IOException io) {
				System.err.print("IO Error");
			}
		}
		return (short)~sum;
	}
	public static short checksum2(byte[] frame) {
		int f = 0, g = 0;
		boolean done = false;
		short word = 0;
		DataInputStream data = makeStream(frame);
		
		while(!done) {
			try {
				f = (f + data.readUnsignedByte()) % 255;
				g = (g + data.readUnsignedByte()) % 255;
			}
			catch(EOFException eof) {
				done = true;
				word = (short)((g << 8) | f);
				try {
					data.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			catch(IOException io) {
				System.err.print("IO Error");
			}
		}
		return (short)~word;
	}
	public static void main(String[] args) 
	{
		System.out.printf("%x\n", checksum1(test2));
	}
}*/
