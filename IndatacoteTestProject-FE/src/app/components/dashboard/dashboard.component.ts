import { EmployeeService } from 'src/app/services/employee.service';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  chartdata: any;
  labeldata: any[] = [];
  realdata: any[] = [];
  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<any>('http://localhost:9090/data')
      .subscribe(
        data => {
          this.chartdata = data;
          console.log(this.chartdata);

          if (this.chartdata != null) {
            for (let i = 0; i < this.chartdata.length; i++) {
              this.labeldata.push(this.chartdata[i].departmentName);
              this.realdata.push(this.chartdata[i].numberOfEmployees);

            }
            console.log(this.labeldata);
            console.log(this.realdata);
            this.RenderChart();
            this.RenderOptionalChart();
            this.RenderPieChart();
            this.RenderOptionalLineChart();

          }
        },
        error => {
          console.error('Error fetching chart data:', error);
        }
      );

  }
  RenderChart() {
    new Chart("myChart", {
      type: 'bar',
      data: {
        labels: this.labeldata,
        datasets: [{
          label: 'Number of Employees',
          data: this.realdata,
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            display: true,
            max:50
          }
        }
      }
    });

  }
  RenderPieChart(){
    new Chart("myPieChart", {
      type: 'pie',
      data: {
        labels: this.labeldata,
        datasets: [{
          label: 'Percentage of Employees in each departement',
          data: this.calculatePercentage(this.realdata),
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            display: false,
          }
        }
      }
    });

  }
  calculatePercentage(data: (number | string)[]): number[] {
    const numericData: number[] = data.map(value => (typeof value === 'string' ? parseFloat(value) : value));
    const total = numericData.reduce((acc, value) => acc + value, 0);
    return numericData.map(value => (value / total) * 100);
  }

  RenderOptionalChart() {
    new Chart("optionalChart", {
      type: 'radar', // Change the chart type to 'radar'
      data: {
        labels: this.labeldata,
        datasets: [{
          label: 'Number of Employees by departement',
          data: this.realdata,
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgb(75, 192, 192)',
          borderWidth: 2
        }]
      },
      options: {
        scales: {
          r: {
            beginAtZero: true,
            max: 50,
            display: true,
          }
        }
      }
    });
  }

  RenderOptionalLineChart() {
    new Chart("optionalLineChart", {
      type: 'line', // Keep the chart type as 'line'
      data: {
        labels: this.labeldata,
        datasets: [{
          label: 'Number of Employees by Departement',
          data: this.realdata,
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgb(75, 192, 192)',
          borderWidth: 2
        }]
      },
      options: {
        scales: {
          x: {
            display: true,
          },
          y: {
            beginAtZero: true,
            max: 50,
            display: true,
          }
        }
      }
    });
  }








}
