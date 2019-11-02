package com.passwordvault.auto.fill

import android.os.CancellationSignal
import android.service.autofill.*

class VaultAutofillService : AutofillService() {

    override fun onFillRequest(fillRequest: FillRequest, cancellationSignal: CancellationSignal, fillCallback: FillCallback) {
    }

    override fun onSaveRequest(saveRequest: SaveRequest, caveCallback: SaveCallback) {
    }
}