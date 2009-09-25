package utils;

import java.io.*;

public final class KeyboardReader
{

    private static final BufferedReader reader;

    static
    {
	reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static String readLine() throws IOException
    {
	return reader.readLine();
    }

    public static Integer readInt() throws NumberFormatException, IOException

    {
	return Integer.parseInt(readLine());
    }

    public static Long readLong() throws NumberFormatException, IOException
    {
	return Long.parseLong(readLine());
    }
}
