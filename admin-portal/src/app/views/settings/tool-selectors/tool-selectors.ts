// views/settings/tool-selectors/tool-selectors.ts
import { Component, computed, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';

interface PipelineTool {
  id: string;
  name: string;
  icon: string;
  version: string;
  latency: string;
  active: boolean;
}

interface ToolCategory {
  key: string;
  label: string;
  icon: string;
}

@Component({
  selector: 'app-tool-selectors',
  standalone: true,
  imports: [MatIconModule, DragDropModule],
  templateUrl: './tool-selectors.html',
  styleUrl: './tool-selectors.scss',
})
export class ToolSelectors {
  categories: ToolCategory[] = [
    { key: 'text-extraction', label: 'Text Extraction', icon: 'document_scanner' },
    { key: 'doc-confirmation', label: 'Doc Confirmation', icon: 'fact_check' },
    { key: 'text-processing', label: 'Text Processing', icon: 'smart_toy' },
    { key: 'text-cleanup', label: 'Text Cleanup', icon: 'cleaning_services' },
  ];

  activeCategory = signal('text-extraction');

  private pipelines = signal<Record<string, PipelineTool[]>>({
    'text-extraction': [
      { id: 'azure', name: 'Azure Form Recognizer', icon: 'cloud', version: 'v3.0 API', latency: '~450ms', active: true },
      { id: 'aws', name: 'AWS Textract', icon: 'cloud_queue', version: 'v2022-11-20', latency: '~520ms', active: true },
      { id: 'internal', name: 'Internal OCR Engine', icon: 'text_snippet', version: 'v1.4 Legacy', latency: '', active: false },
    ],
    'doc-confirmation': [],
    'text-processing': [],
    'text-cleanup': [],
  });

  activeTools = computed(() => this.pipelines()[this.activeCategory()] ?? []);

  categoryCount(key: string): number {
    return this.pipelines()[key]?.length ?? 0;
  }

  priorityLabel(index: number): string {
    return index === 0 ? 'Primary' : `Fallback ${index}`;
  }

  drop(event: CdkDragDrop<PipelineTool[]>): void {
    const category = this.activeCategory();
    this.pipelines.update((all) => {
      const list = [...(all[category] ?? [])];
      moveItemInArray(list, event.previousIndex, event.currentIndex);
      return { ...all, [category]: list };
    });
  }

  toggleActive(tool: PipelineTool): void {
    const category = this.activeCategory();
    this.pipelines.update((all) => ({
      ...all,
      [category]: (all[category] ?? []).map((t) =>
        t.id === tool.id ? { ...t, active: !t.active } : t,
      ),
    }));
  }

  removeTool(tool: PipelineTool): void {
    const category = this.activeCategory();
    this.pipelines.update((all) => ({
      ...all,
      [category]: (all[category] ?? []).filter((t) => t.id !== tool.id),
    }));
  }
}