export interface ApiErrorI {
  title: string;
  message: string;
  statusCode: number;
}

export class ApiError implements ApiErrorI {
  title: string;
  message: string;
  statusCode: number;

  constructor(message: string, statusCode: number | undefined, title: string = 'API Error') {
    this.title = title;
    this.message = message;
    this.statusCode = statusCode ?? 500;
  }
}

export class ValidationError {
  title: string;
  message: string;
  fields: Record<string, string>;

  constructor(message: string, fields: Record<string, string>, title: string = 'Validation Error') {
    this.title = title;
    this.message = message;
    this.fields = fields;
  }

  output() {
    return `${this.message}: ${Object.values(this.fields).join(', ')}`;
  }
}
