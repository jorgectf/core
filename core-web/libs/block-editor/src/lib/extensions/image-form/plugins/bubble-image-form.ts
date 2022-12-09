import { ComponentRef } from '@angular/core';
import { Subject } from 'rxjs';

import { Editor, posToDOMRect } from '@tiptap/core';
import { EditorState, Plugin, PluginKey, Transaction } from 'prosemirror-state';
import { EditorView } from 'prosemirror-view';
import tippy, { Instance, Props } from 'tippy.js';

import { ImageFormComponent } from '../image-form.component';
import { ImageNode } from '../../../nodes/image-node/image.node';
import { DotCMSContentlet } from '@dotcms/dotcms-models';

interface PluginState {
    open: boolean;
}

export interface BubbleLinkFormProps {
    pluginKey: PluginKey;
    editor: Editor;
    element: HTMLElement;
    tippyOptions?: Partial<Props>;
    component?: ComponentRef<ImageFormComponent>;
}

export type BubbleImageFormViewProps = BubbleLinkFormProps & {
    view: EditorView;
};

export class BubbleLinkFormView {
    public editor: Editor;

    public element: HTMLElement;

    public view: EditorView;

    public tippy: Instance | undefined;

    public hola = false;

    public tippyOptions?: Partial<Props>;

    public pluginKey: PluginKey;

    public component?: ComponentRef<ImageFormComponent>;

    private $destroy = new Subject<boolean>();

    constructor({
        editor,
        element,
        view,
        tippyOptions = {},
        pluginKey,
        component
    }: BubbleImageFormViewProps) {
        this.editor = editor;
        this.element = element;
        this.view = view;

        this.tippyOptions = tippyOptions;

        // Detaches menu content from its current parent
        this.element.remove();
        this.element.style.visibility = 'visible';
        this.pluginKey = pluginKey;
        this.component = component;
        this.component.instance.languageId = this.editor.storage.dotConfig.lang;

        this.component.instance.selectedContentlet.subscribe((contentlet) => {
            this.insertImage(contentlet);
            this.hide();
        });

        this.editor.on('focus', () => this.hide());
    }

    update(view: EditorView, prevState?: EditorState): void {
        const next = this.pluginKey?.getState(view.state);
        const prev = prevState ? this.pluginKey?.getState(prevState) : { open: false };

        const { state } = view;
        const { selection } = state;

        if (next?.open === prev?.open || !next?.open) {
            return;
        }

        this.createTooltip();

        this.tippy?.setProps({
            getReferenceClientRect: () => {
                const { from, to } = selection;

                return posToDOMRect(view, from, to);
            }
        });

        this.show();
        this.component.instance.searchContentlets();
        this.component.changeDetectorRef.detectChanges();
    }

    createTooltip() {
        const { element: editorElement } = this.editor.options;
        const editorIsAttached = !!editorElement.parentElement;

        if (this.tippy || !editorIsAttached) {
            return;
        }

        this.tippy = tippy(editorElement.parentElement, {
            ...this.tippyOptions,
            duration: 0,
            content: this.element,
            interactive: true,
            maxWidth: 'none',
            trigger: 'manual',
            placement: 'bottom-start',
            hideOnClick: 'toggle',
            onClickOutside: (instance) => instance.hide(),
            popperOptions: {
                modifiers: [
                    {
                        name: 'flip',
                        options: { fallbackPlacements: ['top-start'] }
                    }
                ]
            }
        });
    }

    insertImage(contentlet: DotCMSContentlet) {
        const { selection } = this.editor.state;
        const { title, fileAsset } = contentlet;
        const node = {
            attrs: {
                data: contentlet,
                src: fileAsset,
                title,
                alt: title
            },
            type: ImageNode.name
        };
        this.editor.chain().insertContentAt(selection.head, node).addNextLine().run();
    }

    close() {
        const transaction = this.editor.state.tr.setMeta(this.pluginKey, {
            open: false
        });
        this.editor.view.dispatch(transaction);
    }

    show() {
        this.tippy?.show();
    }

    hide() {
        this.tippy?.hide();
        this.close();
        // After show the component focus editor
        this.editor.view.focus();
        this.component.instance.resetForm();
        this.component.changeDetectorRef.detectChanges();
    }

    destroy() {
        this.tippy?.destroy();
        this.$destroy.next(true);
        this.component?.destroy();
    }
}

export const bubbleImageFormPlugin = (options: BubbleLinkFormProps) => {
    return new Plugin({
        key: options.pluginKey as PluginKey,
        view: (view) => new BubbleLinkFormView({ view, ...options }),
        state: {
            init(): PluginState {
                return {
                    open: false
                };
            },

            apply(
                transaction: Transaction,
                value: PluginState,
                oldState: EditorState
            ): PluginState {
                const { open } = transaction.getMeta(options.pluginKey) || {};
                const state = options.pluginKey?.getState(oldState);

                if (typeof open === 'boolean') {
                    return { open };
                }

                // keep the old state in case we do not receive a new one.
                return state || value;
            }
        }
    });
};
