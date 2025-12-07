import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  template: `
    <div class="dashboard-container">
      <h1>Dashboard</h1>
      <div class="kpi-grid">
        <mat-card class="kpi-card">
          <mat-card-header>
            <mat-icon mat-card-avatar color="primary">people</mat-icon>
            <mat-card-title>1,234</mat-card-title>
            <mat-card-subtitle>Total Users</mat-card-subtitle>
          </mat-card-header>
        </mat-card>

        <mat-card class="kpi-card">
          <mat-card-header>
            <mat-icon mat-card-avatar color="accent">attach_money</mat-icon>
            <mat-card-title>$50,000</mat-card-title>
            <mat-card-subtitle>Total Revenue</mat-card-subtitle>
          </mat-card-header>
        </mat-card>

        <mat-card class="kpi-card">
          <mat-card-header>
            <mat-icon mat-card-avatar color="warn">dns</mat-icon>
            <mat-card-title>99.9%</mat-card-title>
            <mat-card-subtitle>System Status</mat-card-subtitle>
          </mat-card-header>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container { padding: 20px; }
    .kpi-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      margin-top: 20px;
    }
    .kpi-card {
      padding: 10px;
    }
    .kpi-card mat-icon {
      font-size: 40px;
      height: 40px;
      width: 40px;
    }
  `]
})
export class DashboardComponent {
}
