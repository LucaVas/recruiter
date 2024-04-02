export interface ApiErrorI {
  message: string;
}

export class ApiError implements ApiErrorI {
  message: string;

  constructor(message: string) {
    this.message = message;
  }
}
