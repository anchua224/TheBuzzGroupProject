// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;

// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm: NewEntryForm;

/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        document.getElementById("addCancel")?.addEventListener("click", (e) => {newEntryForm.clearForm();});
        document.getElementById("addButton")?.addEventListener("click", (e) => {newEntryForm.submitForm();});
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        (<HTMLInputElement>document.getElementById("newTitle")).value = "";
        (<HTMLInputElement>document.getElementById("newMessage")).value = "";
    }

     /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitForm() {
        window.alert("Submit form called.");
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let title = "" + (<HTMLInputElement>document.getElementById("newTitle")).value;
        let msg = "" + (<HTMLInputElement>document.getElementById("newMessage")).value;
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }

        // set up an AJAX POST. 
        // When the server replies, the result will go to onSubmitResponse
        const doAjax = async () => {
            await fetch('/messages', {
                method: 'POST',
                body: JSON.stringify({
                    mTitle: title,
                    mMessage: msg
                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then( (response) => {
                // If we get an "ok" message, return the json
                if (response.ok) {
                    return Promise.resolve( response.json() );
                }
                // Otherwise, handle server errors with a detailed popup message
                else{
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then( (data) => {
                newEntryForm.onSubmitResponse(data);
                console.log(data);
            }).catch( (error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            newEntryForm.clearForm();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class NewEntryForm

// Run some configuration code when the web page loads
document.addEventListener('DOMContentLoaded', () => {
  // Create the object that controls the "New Entry" form
  newEntryForm = new NewEntryForm();
  window.alert('DOMContentLoaded');
}, false);