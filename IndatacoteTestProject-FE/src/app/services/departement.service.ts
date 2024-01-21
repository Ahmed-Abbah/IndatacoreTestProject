import { Departement } from './../entities/departement';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DepartementService {






  constructor(private httpClient: HttpClient) { }

  private baseUrl='http://localhost:9090/departements';

  getAllDepartement() :Observable<Departement[]> {
    const url = `${this.baseUrl}`;
    return this.httpClient.get<Departement[]>(url)
      .pipe(
        catchError((error) => {
          console.log(error);
          throw error;
        })
      );
  }
  private deleteUrl='http://localhost:9090/departements';

  deleteDepartement(id: string): Observable<any> {
    const url = `${this.deleteUrl}/${id}`;
    return this.httpClient.delete(url, { responseType: 'text' });
  }


  private addUrl='http://localhost:9090/departements';
  add(client: Departement): Observable<Departement> {
    const url = `${this.addUrl}`;
    return this.httpClient.post<Departement>(url, client);
  }

  private updateUrl='http://localhost:9090/departements';
  edit(departement: Departement): Observable<Departement> {
    const url = `${this.updateUrl}/${departement.id}`;
    return this.httpClient.patch<Departement>(url, departement);
  }
  private departementUrl='http://localhost:9090/departements';

  getDepartementById(id: any):Observable<Departement>{
    const url = `${this.departementUrl}/${id}`;
    return this.httpClient.get<Departement>(url);

  }
}
