import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { Employee } from '../entities/Employee';
@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient) { }

  private baseUrl='http://localhost:9090/employees';

  getAllEmployees() :Observable<Employee[]> {
    const url = `${this.baseUrl}`;
    return this.httpClient.get<Employee[]>(url)
      .pipe(
        catchError((error) => {
          console.log(error);
          throw error;
        })
      );
  }
  private deleteUrl='http://localhost:9090/employees';

  deleteEmployee(id: string): Observable<any> {
    const url = `${this.deleteUrl}/${id}`;
    return this.httpClient.delete(url, { responseType: 'text' });
  }


  private addUrl='http://localhost:9090/employees';
  add(client: Employee): Observable<Employee> {
    const url = `${this.addUrl}`;
    return this.httpClient.post<Employee>(url, client);
  }

  private updateUrl='http://localhost:9090/employees';
  edit(employee: Employee): Observable<Employee> {
    const url = `${this.updateUrl}/${employee.id}`;
    return this.httpClient.patch<Employee>(url,employee);
  }
  private EmployeeUrl='http://localhost:9090/employees';

  getEmployeeById(id: any):Observable<Employee>{
    const url = `${this.EmployeeUrl}/${id}`;
    return this.httpClient.get<Employee>(url);

  }
}
