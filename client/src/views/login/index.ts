import { ref, type Ref } from 'vue';

// const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,64}$/;

export const invalidLoginFields = ref({
  email: {
    invalid: false,
    message: 'Invalid email',
  },
  password: {
    invalid: false,
    message: 'Invalid password',
  },
});

export function isValidLogin(loginDetails, errorMessage: Ref<string>) {
  if (loginDetails.email === '') {
    errorMessage.value = invalidLoginFields.value.email.message;
    invalidLoginFields.value.email.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidLoginFields.value.email.invalid = false;
  }
  if (loginDetails.password === '') {
    errorMessage.value = invalidLoginFields.value.password.message;
    invalidLoginFields.value.password.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidLoginFields.value.password.invalid = false;
  }

  return false;
}
