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

export class ValidationError {
  message: string;
  fields: Record<string, string>;

  constructor(message: string, fields: Record<string, string>) {
    this.message = message;
    this.fields = fields;
  }

  output() {
    return `${this.message}: ${Object.values(this.fields).join(', ')}`;
  }
}
