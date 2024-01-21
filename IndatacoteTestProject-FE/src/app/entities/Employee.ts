import {Departement} from "./departement";

export class Employee {
  id!: string;
  firstName!: string;
  lastName!: string;
  gender!: string;
  salary!: any;
  email!: string;
  joinedYear!: string;
  departement!: Departement;
  departementName!: string;
}
