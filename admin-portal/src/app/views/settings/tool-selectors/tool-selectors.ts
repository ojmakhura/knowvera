// views/settings/tool-selectors/tool-selectors.ts
import { Component, computed, effect, inject, linkedSignal, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ToastrService } from 'ngx-toastr';
import { swalFire } from '@app/@shared/swal';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { SettingsToolSelectors } from '@app/models/bw/co/knowvera/settings/settings-tool-selectors';
import { ToolSelectorDTO } from '@app/models/bw/co/knowvera/settings/tool-selector-dto';
import { Tool } from '@app/models/bw/co/knowvera/settings/tool';
import { LoaderState } from '@app/@shared/loader/loader.state';

class ToolSelectorsModel {
  textExtractionTools: ToolSelectorDTO[] = [];
  documentConfirmationTools: ToolSelectorDTO[] = [];
  textProcessingTools: ToolSelectorDTO[] = [];
  textCleanupTools: ToolSelectorDTO[] = [];
  user: string | any = null;
}

type CategoryKey =
  | 'textExtractionTools'
  | 'documentConfirmationTools'
  | 'textProcessingTools'
  | 'textCleanupTools';

interface ToolCategory {
  key: CategoryKey;
  label: string;
}

const TOOL_META: Record<string, { name: string; icon: string }> = {
  [Tool.TESSERACT]: { name: 'Tesseract OCR', icon: 'document_scanner' },
  [Tool.OLLAMA]: { name: 'Ollama', icon: 'smart_toy' },
  [Tool.LM_STUDIO]: { name: 'LM Studio', icon: 'memory' },
  [Tool.GEMINI]: { name: 'Gemini', icon: 'auto_awesome' },
};

@Component({
  selector: 'app-tool-selectors',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './tool-selectors.html',
  styleUrls: ['./tool-selectors.scss'],
})
export class ToolSelectors implements OnInit {
  settingsApiStore = inject(SettingsApiStore);
  loaderState = inject(LoaderState);
  private toastr = inject(ToastrService);

  loading = linkedSignal(() => this.settingsApiStore.loading());
  loaderMessage = linkedSignal(() => this.settingsApiStore.loaderMessage());
  success = linkedSignal(() => this.settingsApiStore.success());
  error = linkedSignal(() => this.settingsApiStore.error());
  messages = linkedSignal(() => this.settingsApiStore.messages());

  categories: ToolCategory[] = [
    { key: 'textExtractionTools', label: 'Text Extraction' },
    { key: 'documentConfirmationTools', label: 'Doc Confirmation' },
    { key: 'textProcessingTools', label: 'Text Processing' },
    { key: 'textCleanupTools', label: 'Text Cleanup' },
  ];

  activeCategory = signal<CategoryKey>('textExtractionTools');

  activeCategoryLabel = computed(
    () => this.categories.find((cat) => cat.key === this.activeCategory())?.label ?? '',
  );

  toolSelectorsSignal = linkedSignal(() => {
    const storeData = this.settingsApiStore.settingsToolSelectors();
    const model = new ToolSelectorsModel();
    model.textExtractionTools = storeData?.textExtractionTools ?? [];
    model.documentConfirmationTools = storeData?.documentConfirmationTools ?? [];
    model.textProcessingTools = storeData?.textProcessingTools ?? [];
    model.textCleanupTools = storeData?.textCleanupTools ?? [];
    model.user = storeData?.user ?? null;
    return model;
  });

  activeTools = computed(() =>
    [...(this.toolSelectorsSignal()[this.activeCategory()] ?? [])].sort(
      (a, b) => (a.position ?? 0) - (b.position ?? 0),
    ),
  );

  availableTools = computed(() => {
    const used = new Set(this.activeTools().map((t) => t.tool));
    return (Object.values(Tool) as Tool[]).filter((tool) => !used.has(tool));
  });

  constructor() {
    effect(() => {
      this.loaderState.isLoading.set(this.settingsApiStore.loading());
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getSettingsToolSelectors();
  }

  categoryCount(key: CategoryKey): number {
    return this.toolSelectorsSignal()[key]?.length ?? 0;
  }

  toolName(tool: Tool): string {
    return TOOL_META[tool]?.name ?? tool;
  }

  toolIcon(tool: Tool): string {
    return TOOL_META[tool]?.icon ?? 'extension';
  }

  moveUp(index: number): void {
    this.reorder(index, index - 1);
  }

  moveDown(index: number): void {
    this.reorder(index, index + 1);
  }

  async addTool(): Promise<void> {
    const options = this.availableTools();
    if (!options.length) {
      this.toastr.info('All available tools are already configured for this stage');
      return;
    }

    const inputOptions: Record<string, string> = {};
    options.forEach((tool) => (inputOptions[tool] = this.toolName(tool)));

    const result = await swalFire({
      title: 'Add Tool',
      input: 'select',
      inputOptions,
      inputPlaceholder: 'Select a tool',
      showCancelButton: true,
      confirmButtonText: 'Add',
    });

    if (!result.isConfirmed || !result.value) {
      return;
    }

    const category = this.activeCategory();
    const tool = new ToolSelectorDTO();
    tool.tool = result.value as Tool;
    tool.position = this.activeTools().length;

    this.toolSelectorsSignal.update((value) => ({
      ...value,
      [category]: [...(value[category] ?? []), tool],
    }));
    this.persist();
  }

  async removeTool(tool: ToolSelectorDTO): Promise<void> {
    const result = await swalFire({
      title: 'Remove tool?',
      text: `Remove "${this.toolName(tool.tool)}" from this pipeline?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Remove',
      cancelButtonText: 'Cancel',
    });

    if (!result.isConfirmed) {
      return;
    }

    const category = this.activeCategory();
    const reordered = this.activeTools()
      .filter((t) => t !== tool)
      .map((t, i) => ({ ...t, position: i }));

    this.toolSelectorsSignal.update((value) => ({ ...value, [category]: reordered }));
    this.persist();
  }

  private reorder(from: number, to: number): void {
    const list = this.activeTools();
    if (to < 0 || to >= list.length) {
      return;
    }

    const category = this.activeCategory();
    const reordered = [...list];
    [reordered[from], reordered[to]] = [reordered[to], reordered[from]];
    const withPositions = reordered.map((tool, i) => ({ ...tool, position: i }));

    this.toolSelectorsSignal.update((value) => ({ ...value, [category]: withPositions }));
    this.persist();
  }

  private persist(): void {
    const value = this.toolSelectorsSignal();
    const settingsToolSelectors = new SettingsToolSelectors();
    settingsToolSelectors.textExtractionTools = value.textExtractionTools;
    settingsToolSelectors.documentConfirmationTools = value.documentConfirmationTools;
    settingsToolSelectors.textProcessingTools = value.textProcessingTools;
    settingsToolSelectors.textCleanupTools = value.textCleanupTools;
    settingsToolSelectors.user = value.user;

    this.settingsApiStore.saveSettingsToolSelectors({ settingsToolSelectors });
  }
}
