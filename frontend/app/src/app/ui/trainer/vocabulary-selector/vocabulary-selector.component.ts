import {Component} from '@angular/core';
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
            index: portion.vocabularyBlocks.indexOf(block),
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
  }

  /* ------ */
}
