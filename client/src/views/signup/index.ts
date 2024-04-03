import { ref, type Ref } from 'vue';

const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,64}$/;
const emailRegex = /^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/;
const MOBILE_LENGTH = 10;

export const invalidSignupFields = ref({
  username: {
    invalid: false,
    message: 'Invalid username',
  },
  email: {
    invalid: false,
    message: 'Invalid email',
  },
  password: {
    invalid: false,
    message:
      'Invalid password: must have between 8 and 64 characters, and have at least one uppercase letter, one lowercase letter, one number and one special character',
  },
  mobile: {
    invalid: false,
    message: 'Invalid mobile: must have 10 digits',
  },
  city: {
    invalid: false,
    message: 'Invalid city',
  },
  country: {
    invalid: false,
    message: 'Invalid country',
  },
  role: {
    invalid: false,
    message: 'Invalid role',
  },
});

export function isValidSignup(signupDetails, errorMessage: Ref<string>) {
  if (signupDetails.username === '') {
    errorMessage.value = invalidSignupFields.value.username.message;
    invalidSignupFields.value.username.invalid = true;
    return false;
  } else {
    errorMessage.value = '';
    invalidSignupFields.value.username.invalid = false;
  }
  if (signupDetails.email === '' || !signupDetails.email.match(emailRegex)) {
    errorMessage.value = invalidSignupFields.value.email.message;
    invalidSignupFields.value.email.invalid = true;
    return false;
  } else {
    errorMessage.value = '';
    invalidSignupFields.value.email.invalid = false;
  }
  if (signupDetails.password === '' || !signupDetails.password.match(passwordRegex)) {
    errorMessage.value = invalidSignupFields.value.password.message;
    invalidSignupFields.value.password.invalid = true;
    return false;
  } else {
    errorMessage.value = '';
    invalidSignupFields.value.password.invalid = false;
  }
  if (signupDetails.mobile === '' || signupDetails.mobile.length !== MOBILE_LENGTH) {
    errorMessage.value = invalidSignupFields.value.mobile.message;
    invalidSignupFields.value.mobile.invalid = true;
    return false;
  } else {
    errorMessage.value = '';
    invalidSignupFields.value.mobile.invalid = false;
  }
  if (signupDetails.city === '') {
    errorMessage.value = invalidSignupFields.value.city.message;
    invalidSignupFields.value.city.invalid = true;
    return false;
  } else {
    errorMessage.value = '';
    invalidSignupFields.value.city.invalid = false;
  }
  if (signupDetails.country === '') {
    errorMessage.value = invalidSignupFields.value.country.message;
    invalidSignupFields.value.country.invalid = true;
    return false;
  } else {
    errorMessage.value = '';
    invalidSignupFields.value.country.invalid = false;
  }
  if (signupDetails.role === '') {
    errorMessage.value = invalidSignupFields.value.role.message;
    invalidSignupFields.value.role.invalid = true;
    return false;
  } else {
    errorMessage.value = '';
    invalidSignupFields.value.role.invalid = false;
  }

  return true;
}
