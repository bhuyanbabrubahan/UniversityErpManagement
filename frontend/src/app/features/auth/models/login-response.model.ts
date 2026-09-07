import { AuthData } from './auth-data.model';


export interface LoginResponse {

  success: boolean;

  message: string;

  status: number;

  data: AuthData;

  timestamp: string;

  errors: string[] | null;

}