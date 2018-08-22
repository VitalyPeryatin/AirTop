package com.infinity_coder.infinity.airtop.ui.auth.phone_verify;

import com.infinity_coder.infinity.airtop.data.network.response.PhoneAuthResponse;

public interface OnPhoneVerifyListener {
    void onPhoneVerify(PhoneAuthResponse response);
}
