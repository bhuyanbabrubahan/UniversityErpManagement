import { AddressType } from '../enums/address-type.enum';

export interface AddressRequest {
  houseNumber: string;
  street: string;
  landmark?: string;
  addressType: AddressType;
  villageId: number;
  pincode?: string;

  // UI only - not sent to backend
  villageName?: string;
  tehsil?: string;
  district?: string;
  state?: string;
  country?: string;
}