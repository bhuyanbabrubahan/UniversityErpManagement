function n(){return r=>{let s=r.get("newPassword")?.value,a=r.get("confirmPassword")?.value;return!s||!a||s===a?null:{passwordMismatch:!0}}}export{n as a};
