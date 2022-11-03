import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DataService, Portion, VocabularyInPortion} from "../../../data/data.service";

interface PortionNode {
  expanded: boolean;
  name: string;
  blocks: BlockNode[];
}
interface BlockNode {
  index: number;
  vocabularies: VocabularyNode[];
}
interface VocabularyNode {
  selected: boolean;
  vocabulary: VocabularyInPortion;
}

/* selection string */

type Part = {
  subtract: boolean;
  part: string;
};

@Component({
  selector: 'app-vocabulary-selector',
  templateUrl: './vocabulary-selector.component.html',
  styleUrls: ['./vocabulary-selector.component.scss']
})
export class VocabularySelectorComponent {
  portionNodes: PortionNode[] = [];

  constructor(private data: DataService) {
    this.data.getPortions().subscribe((portions: Portion[]) => {
      portions.forEach(portion => {
        let blocks: BlockNode[] = portion.vocabularyBlocks.map(block => {
          let vocabularies: VocabularyNode[] = block.map(vocabulary => {
            return {
              selected: false,
              vocabulary
            };
          });
          return {
            index: portion.vocabularyBlocks.indexOf(block) + 1,
            vocabularies
          };
        });
        this.portionNodes.push({
          name: portion.name,
          expanded: false,
          blocks
        });
      });
    });
  }

  @Output("selectionString") selectionString = new EventEmitter<string>();

  /* --- handlers for node structure --- */

  isPortionExpanded(node: PortionNode) {
    return node.expanded;
  }

  togglePortion(node: PortionNode) {
    node.expanded = !node.expanded;
  }

  isPortionSelected(portion: PortionNode) {
    let selected = true;
    portion.blocks.forEach(block => {
      if (!this.isBlockSelected(block)) selected = false;
    });
    return selected;
  }

  isPortionPartiallySelected(portion: PortionNode) {
    let partiallySelected = false;
    portion.blocks.forEach(block => {
      if (this.isBlockPartiallySelected(block)) partiallySelected = true;
    });
    return partiallySelected;
  }

  selectPortion(portion: PortionNode, selected: boolean) {
    portion.blocks.forEach(block => {
      this.selectBlock(block, selected);
    });
    this.updateSelectionString();
  }

  isBlockSelected(block: BlockNode) {
    let selected = true;
    block.vocabularies.forEach(vocabulary => {
      if (!vocabulary.selected) selected = false;
    });
    return selected;
  }

  isBlockPartiallySelected(block: BlockNode) {
    let partiallySelected = false;
    block.vocabularies.forEach(vocabulary => {
      if (vocabulary.selected) partiallySelected = true;
    });
    return partiallySelected;
  }

  selectBlock(block: BlockNode, selected: boolean) {
    block.vocabularies.forEach(vocabulary => vocabulary.selected = selected);
    this.updateSelectionString();
  }

  updateSelectionString() {
    this.selectionString.emit(this.generateSelectionString());
  }

  /* ------ */

  JOIN_TOKEN = ",";
  SUBTRACT_TOKEN = "-";
  BLOCKS_TOKEN = ":";
  BLOCKS_SEPARATOR_TOKEN = "_";
  NON_NUMBER_PORTION_NAME_TOKEN = "\"";

  generateSelectionString() {
    let parts: Part[] = [];

    this.portionNodes.forEach(portion => {
      if (this.isPortionPartiallySelected(portion)) {
        const portionStr = isNaN(+portion.name)
          ? this.NON_NUMBER_PORTION_NAME_TOKEN + portion.name + this.NON_NUMBER_PORTION_NAME_TOKEN
          : portion.name;
        if (this.isPortionSelected(portion)) {
          parts.push({
            subtract: false,
            part: portionStr
          });
        } else {
          let blocks: BlockNode[] = portion.blocks.filter(block => this.isBlockPartiallySelected(block));

          let blocksForParts: BlockNode[] = [];
          let vocabularyParts: Part[] = [];
          blocks.forEach(block => {
            if (!this.isBlockSelected(block)) {
              let vocabulariesSelected = 0;
              block.vocabularies.forEach(vocabulary => {
                if (vocabulary.selected) vocabulariesSelected++;
              });
              const totalVocabularies = block.vocabularies.length;
              if (vocabulariesSelected / totalVocabularies > 0.5) {

                blocksForParts.push(block);
                block.vocabularies.forEach(vocabulary => {
                  if (!vocabulary.selected) {
                    vocabularyParts.push({
                      subtract: true,
                      part: vocabulary.vocabulary.base_form
                    });
                  }
                });

              } else {

                block.vocabularies.forEach(vocabulary => {
                  if (vocabulary.selected) {
                    vocabularyParts.push({
                      subtract: false,
                      part: vocabulary.vocabulary.base_form
                    });
                  }
                });

              }
            } else {
              blocksForParts.push(block);
            }
          });

          if (blocksForParts.length > 0) {
            parts.push({
              subtract: false,
              part: portionStr + this.BLOCKS_TOKEN + blocksForParts.map(block => block.index).join(this.BLOCKS_SEPARATOR_TOKEN)
            });
          }
          vocabularyParts.forEach(part => parts.push(part));
        }
      }
    });

    let selectionString = "";
    parts.forEach(part => {
      selectionString += part.subtract ? this.SUBTRACT_TOKEN : this.JOIN_TOKEN;
      selectionString += part.part;
    });
    selectionString = selectionString.substring(1);
    return selectionString;
  }
}
