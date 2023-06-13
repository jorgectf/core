package com.dotcms.enterprise.license;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotcms.enterprise.license.bouncycastle.crypto.digests.SHA1Digest;
import com.dotcms.enterprise.license.bouncycastle.crypto.params.RSAKeyParameters;
import com.dotcms.enterprise.license.bouncycastle.crypto.signers.RSADigestSigner;
import com.dotcms.enterprise.license.bouncycastle.util.encoders.Base64;
import com.dotcms.repackage.org.apache.commons.io.IOUtils;
import com.dotmarketing.business.DotStateException;

public class LicenseTransformer{

    public final String license;
    public final DotLicense dotLicense;
    
    public LicenseTransformer(File f){
        try (InputStream is = Files.newInputStream(f.toPath())){
            this.license = IOUtils.toString(is);
            this.dotLicense=toLicense(this.license.getBytes());
        } catch (Exception e) {
            throw new DotStateException(e);
        }
    }
    
    
    public LicenseTransformer(String license){
        this.license = license;
        try {
            this.dotLicense=toLicense(license.getBytes());
        } catch (IOException e) {
            throw new DotStateException(e);
        }
        
    }
    public LicenseTransformer(byte[] licenseB){
        this(new String(licenseB));
    }
    
    /**
     * Initializes the License Manager information by reading the license data file.
     * 
     * @param data - The license data file.
     * @throws IOException An error occurred when accessing or reading the file.
     */
    private DotLicense toLicense(byte[] bytes) throws IOException  {
        Map<String, Object> data = getLicenseData(bytes);
        if (data != null) {
            
            return new DotLicense()
                   .level((Integer) data.get("level"))
                   .validUntil((Date) data.get("validUntil"))
                   .clientName((String) data.get("clientName"))
                   .licenseType((String) data.get("licenseType"))
                   .perpetual((Boolean) data.get("perpetual"))
                   .licenseVersion((Integer) data.get("version"))
                   .serial((String) data.get("serial"));

        } 
        return new DotLicense();
    }
    
    /**
     * Transforms the encrypted information contained in the license data file into human-readable
     * data and puts it inside a {@link Map}.
     * 
     * @param bytes - The byte array containing the license file's data.
     * @return A map with the license data.
     * @throws IOException An error occurred when verifying the license file.
     */
    protected Map<String, Object> getLicenseData(byte[] bytes) throws IOException {
        Map<String, Object> data = new HashMap<String, Object>();

        RSADigestSigner verifier = new RSADigestSigner(new SHA1Digest());
        verifier.init(false, loadPublicKey());

        byte[] unencodedData = Base64.decode(bytes);
        byte[] signature = split(unencodedData, 0, 128);
        byte[] body = split(unencodedData, 128, unencodedData.length - 128);
        verifier.update(body, 0, body.length);
        boolean ok = verifier.verifySignature(signature);
        if (ok) {
            List<byte[]> fields = splitBody(body);
            data.put("clientName", new String(fields.get(0)));
            data.put("issueDate", new Date(byteToLong(fields.get(1))));
            data.put("validUntil", new Date(byteToLong(fields.get(2))));
            data.put("level", byteToInt(fields.get(3)));

            data.put("licenseType", new String(fields.get(5)));
            data.put("perpetual", fields.get(6)[3] == 1);
            data.put("site", fields.get(7)[3] == 1);
            data.put("version", byteToInt(fields.get(8)));
            data.put("serial", new String(fields.get(9)));
            return data;
        }
        return null;
    }
    /**
     * Utility method that transforms a byte array into an integer.
     * 
     * @param b
     * @return
     */
    private int byteToInt(byte[] b) {
        return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + (b[3] & 0xFF);
    }

    /**
     * Utility method that transforms a byte array into a long.
     * 
     * @param b
     * @return
     */
    private long byteToLong(byte[] b) {

        return ((long) b[0] << 56) + ((long) (b[1] & 0xFF) << 48) + ((long) (b[2] & 0xFF) << 40)
                        + ((long) (b[3] & 0xFF) << 32) + ((long) (b[4] & 0xFF) << 24)
                        + ((long) (b[5] & 0xFF) << 16) + ((long) (b[6] & 0xFF) << 8)
                        + (long) (b[7] & 0xFF);
    }

    /**
     * 
     * @param array
     * @return
     */
    private List<byte[]> splitBody(byte[] array) {
        int pos = 0;
        List<byte[]> ret = new ArrayList<byte[]>();
        while (pos < array.length) {
            int size = byteToInt(split(array, pos, 4));
            pos += 4;
            byte[] data = split(array, pos, size);
            pos += size;
            ret.add(data);

        }
        return ret;
    }

    /**
     * Utility method that splits a byte array into a smaller section.
     * 
     * @param array - The array to split.
     * @param start - The start position that will be contained in the new array.
     * @param length - The offset of the new array.
     * @return The new sub-array.
     */
    private byte[] split(byte[] array, int start, int length) {
        byte[] ret = new byte[length];
        for (int i = 0; i < length; i++) {
            ret[i] = array[i + start];
        }
        return ret;
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    private RSAKeyParameters loadPublicKey() throws IOException {
        String[] licData = getLicData();
        BigInteger mod = new BigInteger(licData[1]);
        BigInteger exponent = new BigInteger(licData[2]);
        RSAKeyParameters key = new RSAKeyParameters(false, mod, exponent);
        return key;
    }
    /**
     * 
     * @return
     * @throws IOException
     */
    private String[] getLicData() throws IOException {
        return IOUtils.toString(this.getClass().getResourceAsStream("lic.dat")).split("\\r?\\n");
    }

 
}
