import { HttpClient, HttpEventType } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { NgxFileDropEntry } from 'ngx-file-drop';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.css']
})
export class UploadFileComponent  {

  public files: NgxFileDropEntry[] = [];
  public uploading: boolean = false;
  public uploadProgress: number = 0;
  public formData = new FormData();

  constructor(private http: HttpClient,private router:Router) { }


  public dropped(files: NgxFileDropEntry[]) {
    this.files = files;
  }

  public fileOver(event: any) {

  }

  public fileLeave(event: any) {

  }

  public uploadFiles() {
    this.uploading = true;
    this.uploadProgress = 0;
    this.formData = new FormData();

    for (const droppedFile of this.files) {
      if (droppedFile.fileEntry.isFile) {
        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        fileEntry.file((file: File) => {


          console.log(droppedFile.relativePath, file);


          this.formData.append('file', file, droppedFile.relativePath);

          this.http.post('http://localhost:9090/upload', this.formData, {
            reportProgress: true,
            observe: 'events'
          }).subscribe(event => {
            if (event.type === HttpEventType.UploadProgress) {
              this.uploadProgress = Math.round((event.loaded / event.total!) * 100);
            } else if (event.type === HttpEventType.Response) {
              this.uploading = false;
              // Sanitized logo returned from backend
              console.log("Upload completed:", event.body);
              Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Csv File uploaded successfully',
                showConfirmButton: false,
                timer: 1500
              })
              this.router.navigate(['/dashboard']);
            }
          });
        });
      } else {
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }

}
