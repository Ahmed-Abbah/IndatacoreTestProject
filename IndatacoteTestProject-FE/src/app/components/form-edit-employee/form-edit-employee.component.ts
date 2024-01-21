import { EmployeeService } from 'src/app/services/employee.service';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Departement } from 'src/app/entities/departement';
import { DepartementService } from 'src/app/services/departement.service';
import Swal from 'sweetalert2';
import { Employee } from 'src/app/entities/Employee';

@Component({
  selector: 'app-form-edit-employee',
  templateUrl: './form-edit-employee.component.html',
  styleUrls: ['./form-edit-employee.component.css']
})
export class FormEditEmployeeComponent implements OnInit {

  constructor(private fb: FormBuilder, private employeeService:EmployeeService, private departementService: DepartementService, private router: Router, private httpClient:HttpClient, private route: ActivatedRoute) {}
  editForm : FormGroup = this.fb.group({
    id: [''],
    firstName: [''],
    lastName: [''],
    email: [''],
    joinedYear: [''],
    salary: [''],
    departement: undefined,
  });
  departements?: Departement[];



  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.employeeService.getEmployeeById(id).subscribe((employee:Employee) => {
        console.log(employee);
        this.editForm.patchValue(employee);
      });
      console.log(id);
    });
    this.getAllDepartement();
  }
  getAllDepartement(): void {
    this.departementService.getAllDepartement().subscribe(
      (departements) => {
        this.departements = departements;
      },
      (error) => {
        console.error(error);
      }
    );
  }
  onSubmit() {
    console.log(this.editForm.value);
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }
    let employee : Employee = new Employee();
    let departement = new Departement();

     this.route.params.subscribe(params => {
      employee.id = params['id'];
      console.log(employee.id);
    });
    employee.firstName = this.editForm.value.firstName!;
    employee.lastName = this.editForm.value.lastName!;
    employee.email = this.editForm.value.email!;
    employee.salary = String(this.editForm.value.salary);
    employee.joinedYear = String(this.editForm.value.joinedYear);
    const departementId = this.editForm.value.departement!;
    const selectedDepartement = this.departements?.find(departement => departement.id == departementId);
    employee.departement = selectedDepartement!;


    this.employeeService.edit(employee).subscribe((response) => {
      console.log(response);
      if(response.id!=null){
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Employee updated successfully',
          showConfirmButton: false,
          timer: 1500
        })
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.router.navigate(["gestion-employee"]);
        });
      }
    });


  }


  get code() {
    return this.editForm.get('code');
  }
  get name() {
    return this.editForm.get('name');
  }

}
