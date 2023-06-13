package com.dotcms.enterprise.license.bouncycastle.util.encoders;

/**
 * Convert binary data to and from UrlBase64 encoding.  This is identical to
 * Base64 encoding, except that the padding character is "." and the other 
 * non-alphanumeric characters are "-" and "_" instead of "+" and "/".
 * <p>
 * The purpose of UrlBase64 encoding is to provide a compact encoding of binary
 * data that is safe for use as an URL parameter. Base64 encoding does not
 * produce encoded values that are safe for use in URLs, since "/" can be 
 * interpreted as a path delimiter; "+" is the encoded form of a space; and
 * "=" is used to separate a name from the corresponding value in an URL 
 * parameter.
 */
public class UrlBase64Encoder extends Base64Encoder
{
    public UrlBase64Encoder()
    {
        encodingTable[encodingTable.length - 2] = (byte) '-';
        encodingTable[encodingTable.length - 1] = (byte) '_';
        padding = (byte) '.';
        // we must re-create the decoding table with the new encoded values.
        initialiseDecodingTable();
    }
}
