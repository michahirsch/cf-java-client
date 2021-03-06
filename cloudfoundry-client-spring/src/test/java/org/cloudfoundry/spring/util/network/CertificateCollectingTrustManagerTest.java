/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.spring.util.network;

import org.junit.Test;
import org.mockito.Mockito;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class CertificateCollectingTrustManagerTest {

    private final X509Certificate[] chain = new X509Certificate[0];

    private final X509TrustManager delegate = mock(X509TrustManager.class, Mockito.RETURNS_SMART_NULLS);

    private final CertificateCollectingTrustManager trustManager = new CertificateCollectingTrustManager(this.delegate);

    @Test(expected = IllegalStateException.class)
    public void checkClientTrustedAlreadyCollected() throws CertificateException {
        this.trustManager.checkClientTrusted(this.chain, null);
        this.trustManager.checkClientTrusted(this.chain, null);
    }

    @Test
    public void checkClientTrustedNotTrusted() throws CertificateException {
        doThrow(new CertificateException()).when(this.delegate).checkClientTrusted(this.chain, null);

        this.trustManager.checkClientTrusted(this.chain, null);

        assertFalse(this.trustManager.isTrusted());
    }

    @Test
    public void checkClientTrustedTrusted() throws CertificateException {
        this.trustManager.checkClientTrusted(this.chain, null);

        assertTrue(this.trustManager.isTrusted());
    }

    @Test(expected = IllegalStateException.class)
    public void checkServerTrustedAlreadyCollected() throws CertificateException {
        this.trustManager.checkServerTrusted(this.chain, null);
        this.trustManager.checkServerTrusted(this.chain, null);
    }

    @Test
    public void checkServerTrustedNotTrusted() throws CertificateException {
        doThrow(new CertificateException()).when(this.delegate).checkServerTrusted(this.chain, null);

        this.trustManager.checkServerTrusted(this.chain, null);

        assertFalse(this.trustManager.isTrusted());
    }

    @Test
    public void checkServerTrustedTrusted() throws CertificateException {
        this.trustManager.checkServerTrusted(this.chain, null);

        assertTrue(this.trustManager.isTrusted());
    }

    @Test
    public void getAcceptedIssuers() {
        this.trustManager.getAcceptedIssuers();

        verify(this.delegate).getAcceptedIssuers();
    }

    @Test
    public void getCollectedCertificateChain() throws CertificateException {
        this.trustManager.checkServerTrusted(this.chain, null);

        assertEquals(0, this.trustManager.getCollectedCertificateChain().length);
        assertNotSame(this.chain, this.trustManager.getCollectedCertificateChain());
    }

    @Test
    public void getCollectedCertificateChainNotCollected() {
        assertNull(this.trustManager.getCollectedCertificateChain());
    }

    @Test
    public void isTrusted() {
        assertFalse(this.trustManager.isTrusted());
    }
}
