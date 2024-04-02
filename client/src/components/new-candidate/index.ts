import { ref, type Ref } from 'vue';

const emailRegex = /^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/;
const PAN_LENGTH = 10;
const PHONE_LENGTH = 10;

export const invalidFields = ref({
  name: {
    invalid: false,
    message: 'Invalid name',
  },
  phone: {
    invalid: false,
    message: 'Invalid phone: must be 10 digits long',
  },
  email: {
    invalid: false,
    message: 'Invalid email',
  },
  pan: {
    invalid: false,
    message: 'Invalid PAN: must be 10 digits long',
  },
  education: {
    invalid: false,
    message: 'Invalid education',
  },
  totalExperience: {
    invalid: false,
    message: 'Invalid total experience: must be a number.',
  },
  currentCtc: {
    invalid: false,
    message: 'Invalid current CTC: must be a number.',
  },
});

export function invalidCandidate(candidateDetails, errorMessage: Ref<string>) {
  if (candidateDetails.name === '') {
    errorMessage.value = invalidFields.value.name.message;
    invalidFields.value.name.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidFields.value.name.invalid = false;
  }
  if (candidateDetails.phone === '' || candidateDetails.phone.length !== PHONE_LENGTH) {
    errorMessage.value = invalidFields.value.phone.message;
    invalidFields.value.phone.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidFields.value.phone.invalid = false;
  }
  if (candidateDetails.email === '' || !candidateDetails.email.match(emailRegex)) {
    errorMessage.value = invalidFields.value.email.message;
    invalidFields.value.email.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidFields.value.email.invalid = false;
  }
  if (candidateDetails.pan === '' || candidateDetails.pan.length !== PAN_LENGTH) {
    errorMessage.value = invalidFields.value.pan.message;
    invalidFields.value.pan.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidFields.value.pan.invalid = false;
  }
  if (candidateDetails.education === '') {
    errorMessage.value = invalidFields.value.education.message;
    invalidFields.value.education.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidFields.value.education.invalid = false;
  }
  if (candidateDetails.totalExperience === '') {
    errorMessage.value = invalidFields.value.totalExperience.message;
    invalidFields.value.totalExperience.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidFields.value.totalExperience.invalid = false;
  }
  if (candidateDetails.currentCtc === '') {
    errorMessage.value = invalidFields.value.currentCtc.message;
    invalidFields.value.currentCtc.invalid = true;
    return true;
  } else {
    errorMessage.value = '';
    invalidFields.value.currentCtc.invalid = false;
  }

  return false;
}
