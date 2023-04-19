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

package com.dotcms.enterprise.license.bouncycastle.asn1.esf;

import com.dotcms.enterprise.license.bouncycastle.asn1.*;

public class SignaturePolicyId
    extends ASN1Encodable
{
    private DERObjectIdentifier  sigPolicyId;
    private OtherHashAlgAndValue sigPolicyHash;
    private SigPolicyQualifiers  sigPolicyQualifiers;


    public static SignaturePolicyId getInstance(
        Object obj)
    {
        if (obj == null || obj instanceof SignaturePolicyId)
        {
            return (SignaturePolicyId) obj;
        }
        else if (obj instanceof ASN1Sequence)
        {
            return new SignaturePolicyId((ASN1Sequence) obj);
        }

        throw new IllegalArgumentException(
                "Unknown object in 'SignaturePolicyId' factory : "
                        + obj.getClass().getName() + ".");
    }

    public SignaturePolicyId(
        ASN1Sequence seq)
    {
        if (seq.size() != 2 && seq.size() != 3)
        {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        }

        sigPolicyId = DERObjectIdentifier.getInstance(seq.getObjectAt(0));
        sigPolicyHash = OtherHashAlgAndValue.getInstance(seq.getObjectAt(1));

        if (seq.size() == 3)
        {
            sigPolicyQualifiers = SigPolicyQualifiers.getInstance(seq.getObjectAt(2));
        }
    }

    public SignaturePolicyId(
        DERObjectIdentifier   sigPolicyIdentifier,
        OtherHashAlgAndValue  sigPolicyHash)
    {
        this(sigPolicyIdentifier, sigPolicyHash, null);
    }

    public SignaturePolicyId(
        DERObjectIdentifier   sigPolicyId,
        OtherHashAlgAndValue  sigPolicyHash,
        SigPolicyQualifiers   sigPolicyQualifiers)
    {
        this.sigPolicyId = sigPolicyId;
        this.sigPolicyHash = sigPolicyHash;
        this.sigPolicyQualifiers = sigPolicyQualifiers;
    }

    public DERObjectIdentifier getSigPolicyId()
    {
        return sigPolicyId;
    }

    public OtherHashAlgAndValue getSigPolicyHash()
    {
        return sigPolicyHash;
    }

    public SigPolicyQualifiers getSigPolicyQualifiers()
    {
        return sigPolicyQualifiers;
    }

    /**
     * <pre>
     * SignaturePolicyId ::= SEQUENCE {
     *     sigPolicyId SigPolicyId,
     *     sigPolicyHash SigPolicyHash,
     *     sigPolicyQualifiers SEQUENCE SIZE (1..MAX) OF SigPolicyQualifierInfo OPTIONAL}
     * </pre>
     */
    public DERObject toASN1Object()
    {
        ASN1EncodableVector  v = new ASN1EncodableVector();

        v.add(sigPolicyId);
        v.add(sigPolicyHash);
        if (sigPolicyQualifiers != null)
        {
            v.add(sigPolicyQualifiers);
        }

        return new DERSequence(v);
    }
}
