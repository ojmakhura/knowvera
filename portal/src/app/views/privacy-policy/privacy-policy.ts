import { Component, ChangeDetectionStrategy, OnInit, inject } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { TranslateModule } from '@ngx-translate/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-privacy-policy',
  imports: [TranslateModule, MatCardModule],
  templateUrl: './privacy-policy.html',
  styleUrl: './privacy-policy.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PrivacyPolicy implements OnInit {
  private readonly title = inject(Title);
  private readonly meta = inject(Meta);
  
  protected readonly lastUpdated = 'January 23, 2026';

  ngOnInit(): void {
    this.title.setTitle('Privacy Policy - Central KYC Portal');
    this.meta.updateTag({ 
      name: 'description', 
      content: 'Privacy Policy for Central KYC Portal. Learn how we collect, use, and protect your personal information in our KYC verification services.' 
    });
    this.meta.updateTag({ 
      property: 'og:title', 
      content: 'Privacy Policy - Central KYC Portal' 
    });
    this.meta.updateTag({ 
      property: 'og:description', 
      content: 'Privacy Policy for Central KYC Portal. Learn how we collect, use, and protect your personal information.' 
    });
    this.meta.updateTag({ 
      property: 'og:type', 
      content: 'website' 
    });
  }
}
