const validPasswordRegex = '^(?=.*d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,64}$';
const validEmailRegex =
  "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

export const errorMessages = {
  invalidPassword: {
    message:
      'Both passwords must be between 8 and 64 characters, and contain at least 1 lowercase letter, 1 uppercase letter and 1 number.',
  },
  samePasswords: {
    message: 'The two passwords entered are identical.',
  },
  invalidEmail: {
    message: 'The email you entered is invalid',
  },
  invalidPhone: {
    message: 'Invalid phone: it must be 10 digits long.',
  },
  invalidCity: {
    message: 'Invalid city: it must be between 3 and 50 characters long.',
  },
};

export const isCityValid = (city: string): boolean => {
  return city.trim().length > 3 && city.trim().length < 50;
};

export const isPhoneValid = (phone: string): boolean => {
  return phone.trim().length === 10;
};

export const isPasswordValid = (password: string): boolean => {
  return !!password.trim().match(validPasswordRegex);
};

export const isEmailValid = (email: string): boolean => {
  return !!email.trim().match(validEmailRegex);
};
