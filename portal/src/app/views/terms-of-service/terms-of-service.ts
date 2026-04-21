import { Component, ChangeDetectionStrategy, OnInit, inject } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { TranslateModule } from '@ngx-translate/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-terms-of-service',
  imports: [TranslateModule, MatCardModule],
  templateUrl: './terms-of-service.html',
  styleUrl: './terms-of-service.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TermsOfService implements OnInit {
  private readonly title = inject(Title);
  private readonly meta = inject(Meta);
  
  protected readonly effectiveDate = 'January 23, 2026';

  ngOnInit(): void {
    this.title.setTitle('Terms of Service - Central KYC Portal');
    this.meta.updateTag({ 
      name: 'description', 
      content: 'Terms of Service for Central KYC Portal. Review the terms and conditions governing the use of our KYC verification platform.' 
    });
    this.meta.updateTag({ 
      property: 'og:title', 
      content: 'Terms of Service - Central KYC Portal' 
    });
    this.meta.updateTag({ 
      property: 'og:description', 
      content: 'Terms of Service for Central KYC Portal. Review the terms and conditions governing our services.' 
    });
    this.meta.updateTag({ 
      property: 'og:type', 
      content: 'website' 
    });
  }
}
