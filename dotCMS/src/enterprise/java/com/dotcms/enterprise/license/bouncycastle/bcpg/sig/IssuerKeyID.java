/* 
* Licensed to dotCMS LLC under the dotCMS Enterprise License (the
* “Enterprise License”) found below 
* 
* Copyright (c) 2023 dotCMS Inc.
* 
* With regard to the dotCMS Software and this code:
* 
* This software, source code and associated documentation files (the
* "Software")  may only be modified and used if you (and any entity that
* you represent) have:
* 
* 1. Agreed to and are in compliance with, the dotCMS Subscription Terms
* of Service, available at https://www.dotcms.com/terms (the “Enterprise
* Terms”) or have another agreement governing the licensing and use of the
* Software between you and dotCMS. 2. Each dotCMS instance that uses
* enterprise features enabled by the code in this directory is licensed
* under these agreements and has a separate and valid dotCMS Enterprise
* server key issued by dotCMS.
* 
* Subject to these terms, you are free to modify this Software and publish
* patches to the Software if you agree that dotCMS and/or its licensors
* (as applicable) retain all right, title and interest in and to all such
* modifications and/or patches, and all such modifications and/or patches
* may only be used, copied, modified, displayed, distributed, or otherwise
* exploited with a valid dotCMS Enterprise license for the correct number
* of dotCMS instances.  You agree that dotCMS and/or its licensors (as
* applicable) retain all right, title and interest in and to all such
* modifications.  You are not granted any other rights beyond what is
* expressly stated herein.  Subject to the foregoing, it is forbidden to
* copy, merge, publish, distribute, sublicense, and/or sell the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
* 
* For all third party components incorporated into the dotCMS Software,
* those components are licensed under the original license provided by the
* owner of the applicable component.
*/

package com.dotcms.enterprise.license.bouncycastle.bcpg.sig;

import com.dotcms.enterprise.license.bouncycastle.bcpg.SignatureSubpacket;
import com.dotcms.enterprise.license.bouncycastle.bcpg.SignatureSubpacketTags;

/**
 * packet giving signature creation time.
 */
public class IssuerKeyID 
    extends SignatureSubpacket
{
    protected static byte[] keyIDToBytes(
        long    keyId)
    {
        byte[]    data = new byte[8];
        
        data[0] = (byte)(keyId >> 56);
        data[1] = (byte)(keyId >> 48);
        data[2] = (byte)(keyId >> 40);
        data[3] = (byte)(keyId >> 32);
        data[4] = (byte)(keyId >> 24);
        data[5] = (byte)(keyId >> 16);
        data[6] = (byte)(keyId >> 8);
        data[7] = (byte)keyId;
        
        return data;
    }
    
    public IssuerKeyID(
        boolean    critical,
        byte[]     data)
    {
        super(SignatureSubpacketTags.ISSUER_KEY_ID, critical, data);
    }
    
    public IssuerKeyID(
        boolean    critical,
        long       keyID)
    {
        super(SignatureSubpacketTags.ISSUER_KEY_ID, critical, keyIDToBytes(keyID));
    }
    
    public long getKeyID()
    {
        long    keyID = ((long)(data[0] & 0xff) << 56) | ((long)(data[1] & 0xff) << 48) | ((long)(data[2] & 0xff) << 40) | ((long)(data[3] & 0xff) << 32)
                                | ((long)(data[4] & 0xff) << 24) | ((data[5] & 0xff) << 16) | ((data[6] & 0xff) << 8) | (data[7] & 0xff);
        
        return keyID;
    }
}
