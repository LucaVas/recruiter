export interface ApiErrorI {
  message: string;
  statusCode: number;
  
}

export class ApiError implements ApiErrorI {
  message: string;
  statusCode: number;

  constructor(message: string, statusCode: number | undefined) {
    this.message = message;
    this.statusCode = statusCode ?? 500;
  }
}
