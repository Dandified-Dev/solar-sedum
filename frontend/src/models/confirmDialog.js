import Swal from "sweetalert2";

class ConfirmDialog{
    static async showConfirmationDialog(confirmButtonText, callback) {
        await Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: `Yes, delete ${confirmButtonText}!`
        }).then(async (result) => {
            if (result.isConfirmed) {
                callback();
            }
        });
    }

    static showWarningDialog(warningText){
        Swal.fire({
            title: "Warning!",
            text: warningText,
            icon: "warning"
        }).then(() => {

        })
    }
}

export default ConfirmDialog;