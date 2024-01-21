import { Employee } from '../../entities/Employee';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { Departement } from 'src/app/entities/departement';
import { DepartementService } from 'src/app/services/departement.service';
import { EmployeeService } from 'src/app/services/employee.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-gestion-employee',
  templateUrl: './gestion-employee.component.html',
  styleUrls: ['./gestion-employee.component.css']
})
export class GestionEmployeeComponent implements OnInit {

  departements?: Departement[];
  employees?: Employee[];
  showEditForm: boolean = false;
  selectedId?: string;
  constructor(private fb: FormBuilder, private departementService: DepartementService, private employeeService: EmployeeService, private router: Router, private httpClient:HttpClient) {}
  editForm = this.fb.group({
    id: [''],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', Validators.required],
    gender: ['', Validators.required],
    salary: ['', Validators.required],
    joinedYear: ['', Validators.required],
    departement: ['', Validators.required],
  });


  ngOnInit(): void {
    this.getAllDepartement();
    this.getAllEmployees();

  }
  getAllEmployees(): void{
    this.employeeService.getAllEmployees().subscribe(
      (employees : Employee[]) : void => {
        this.employees = employees;
        console.log(this.employees);
        setTimeout(() => {
          $('#datatableexample').DataTable({
            pagingType: 'full_numbers',
            pageLength: 5,
            processing: true,
            lengthMenu: [5, 10, 25],
          });
        }, 1);
      },
      (error) : void => {
        console.error(error);
      }
    );
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

  onDelete(id: string) {
    this.employeeService.deleteEmployee(id).subscribe((response) => {
      console.log(response);
      if(response!=null){
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Employee deleted successfully',
          showConfirmButton: false,
          timer: 1500
        })
      }

      this.getAllEmployees();
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(["gestion-employee"]);
      });
    });
  }
  onEdit(id: string) {
    this.employeeService.deleteEmployee(id).subscribe((response) => {
      console.log(response);
      this.getAllEmployees();
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(["gestion-employee"]);
      });
    });
  }

  onSubmit() {
    //console.log(this.editForm.value);
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const firstName = this.editForm.value.firstName!;
    const lastName = this.editForm.value.lastName!;
    const gender = this.editForm.value.gender!;
    const salary = this.editForm.value.salary!;
    const email = this.editForm.value.email!;
    const joinedYear = +this.editForm.value.joinedYear!;
    const departementId = this.editForm.value.departement!;
    const selectedDepartement = this.departements?.find(departement => departement.id == departementId);

// Now you can use these variables to create an instance of the Employee class


  if (!selectedDepartement) {
    console.error(`Department with ID ${departementId} not found.`);
    return;
  }
    console.log(departementId);
    let employee : Employee = new Employee();
    employee.firstName = firstName;
    employee.lastName = lastName;
    employee.email = email;
    employee.departement = selectedDepartement;
    employee.joinedYear=String(joinedYear);
    employee.gender=gender;
    employee.salary=salary;
            this.employeeService.add(employee).pipe(
              catchError((error: HttpErrorResponse) => {
                console.error('HTTP Error:', error.status, error.statusText);
                if (error.status === 500) {
                  Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: 'Internal server error',
                    showConfirmButton: false,
                    timer: 1500
                  });
                  this.editForm.reset();
                }

                if(error.status === 404){
                  Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: 'Not found',
                    showConfirmButton: false,
                    timer: 1500
                  });
                }


                console.log(error);
                return throwError('Something went wrong. Please try again later.');
              })
            )
            .subscribe((response) => {
              console.log(response);
              this.getAllDepartement();

                Swal.fire({
                  position: 'center',
                  icon: 'success',
                  title: 'Employee added successfully',
                  showConfirmButton: false,
                  timer: 1500
                });

              this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
                this.router.navigate(["gestion-employee"]);
              });
            });


}

toggleEditForm() {
  this.showEditForm = true;
}
}

