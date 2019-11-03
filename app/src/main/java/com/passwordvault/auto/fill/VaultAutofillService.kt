package com.passwordvault.auto.fill

import android.R
import android.app.assist.AssistStructure
import android.os.CancellationSignal
import android.service.autofill.*
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews

class VaultAutofillService : AutofillService() {

    override fun onFillRequest(request: FillRequest, cancellationSignal: CancellationSignal, callback: FillCallback) {
        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context[context.size - 1].structure


        val parsedStructure: ParsedStructure? = parseStructure(structure)
        parsedStructure?.let {
            val (username: String, password: String) = Pair<String, String>("UserName", "Password")

            val usernamePresentation = RemoteViews(packageName, R.layout.simple_list_item_1)
            usernamePresentation.setTextViewText(android.R.id.text1, "my_username")
            val passwordPresentation = RemoteViews(packageName, android.R.layout.simple_list_item_1)
            passwordPresentation.setTextViewText(android.R.id.text1, "Password for my_username")

            val fillResponse = FillResponse.Builder()
                .addDataset(
                    Dataset.Builder()
                        .setValue(
                            parsedStructure.usernameId,
                            AutofillValue.forText(username),
                            usernamePresentation
                        )
                        .setValue(
                            parsedStructure.passwordId,
                            AutofillValue.forText(password),
                            passwordPresentation
                        )
                        .build()
                )
                .build()

            callback.onSuccess(fillResponse)
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
    }

    fun parseStructure(structure: AssistStructure): ParsedStructure? {
        val node = structure.getWindowNodeAt(0).rootViewNode
        val list = mutableListOf<AutofillId>()

        (0..node.childCount - 1).forEach {

            if (node.className.contains("EditText")) {
                val id = node.autofillId
                id?.let {
                    if (node.idEntry.contains("username")) {
                        list.add(id)
                    }
                    if (node.idEntry.contains("email")) {
                        list.add(id)
                    }
                }
            }
        }

        if (list.size != 2) {
            return null
        }

        val parsedStructure = ParsedStructure(list[0], list[1])
        return parsedStructure
    }

    data class ParsedStructure(var usernameId: AutofillId, var passwordId: AutofillId)

    data class UserData(var username: String, var password: String)
}