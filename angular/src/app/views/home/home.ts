import { computed, Signal, signal } from '@angular/core';
import { AfterViewInit, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { MaterialModule } from '@app/material.module';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AnalyticsApiStore } from '@app/store/bw/co/centralkyc/analytics/analytics-api.store';

interface StatCard {
  title: string;
  value: Signal<number>;
  icon: string;
  color: string;
  trend?: string;
}

interface QuickAction {
  title: string;
  description: string;
  icon: string;
  route: string;
  color: string;
}

interface Feature {
  title: string;
  description: string;
  icon: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [TranslateModule, MaterialModule, CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnInit, AfterViewInit, OnDestroy  {
  private router = inject(Router);
  protected analyticsApiStore = inject(AnalyticsApiStore);

  statCards: StatCard[] = [
    {
      title: 'Total Individuals',
      value: computed(() => this.analyticsApiStore.data()?.individualCount || 0),
      icon: 'people',
      color: '#3f51b5',
      trend: '+12%'
    },
    {
      title: 'Pending Verifications',
      value: computed(() => this.analyticsApiStore.data()?.pendingVerificationCount || 0),
      icon: 'pending_actions',
      color: '#ff9800',
      trend: '-5%'
    },
    {
      title: 'Organizations',
      value: computed(() => this.analyticsApiStore.data()?.organisationCount || 0),
      icon: 'business',
      color: '#4caf50',
      trend: '+8%'
    },
    {
      title: 'Documents Processed',
      value: computed(() => this.analyticsApiStore.data()?.documentCount || 0),
      icon: 'description',
      color: '#9c27b0',
      trend: '+23%'
    }
  ];

  quickActions: QuickAction[] = [
    {
      title: 'New Individual',
      description: 'Register a new individual for KYC verification',
      icon: 'person_add',
      route: '/individual/edit',
      color: '#3f51b5'
    },
    {
      title: 'New Organisation',
      description: 'Add a new organisation to the system',
      icon: 'add_business',
      route: '/organisation/edit',
      color: '#4caf50'
    },
    {
      title: 'Upload Document',
      description: 'Upload and verify identity documents',
      icon: 'upload_file',
      route: '/document/upload',
      color: '#ff9800'
    },
    {
      title: 'View Reports',
      description: 'Access compliance and verification reports',
      icon: 'assessment',
      route: '/reports',
      color: '#9c27b0'
    }
  ];

  features: Feature[] = [
    {
      title: 'Automated Verification',
      description: 'AI-powered document verification and validation for faster processing',
      icon: 'verified_user'
    },
    {
      title: 'Compliance Management',
      description: 'Stay compliant with global KYC/AML regulations and standards',
      icon: 'gavel'
    },
    {
      title: 'Secure Storage',
      description: 'Bank-level encryption for all sensitive customer data and documents',
      icon: 'lock'
    },
    {
      title: 'Real-time Monitoring',
      description: 'Track verification status and get instant notifications',
      icon: 'notifications_active'
    },
    {
      title: 'Multi-tenant Support',
      description: 'Manage multiple organizations with role-based access control',
      icon: 'corporate_fare'
    },
    {
      title: 'API Integration',
      description: 'Seamless integration with your existing systems and workflows',
      icon: 'integration_instructions'
    }
  ];

  constructor() {
    this.analyticsApiStore.countAnalytics();
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void {
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  getTrendIcon(trend?: string): string {
    if (!trend) return '';
    return trend.startsWith('+') ? 'trending_up' : 'trending_down';
  }

  getTrendClass(trend?: string): string {
    if (!trend) return '';
    return trend.startsWith('+') ? 'trend-up' : 'trend-down';
  }
}
