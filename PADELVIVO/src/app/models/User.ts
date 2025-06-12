export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  image: string;
  enabled: boolean;
  password: string;
  createdDate?: string; // ISO string, puede estar indefinida
  lastModifiedDate?: string;
  lastPasswordChangeDate?: string;
   roles?: string[];
}
