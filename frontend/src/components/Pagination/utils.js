import { range, length, last, head } from 'ramda';

export const pagination = ({ selectedPage, maxPage, paginatorWidth }) => {
  if (isNaN(maxPage) || isNaN(paginatorWidth) || isNaN(selectedPage) || (paginatorWidth < 1)) {
    return [];
  }
  if (maxPage <= paginatorWidth) return range(1, maxPage + 1);
  let result = [selectedPage];
  let cursor = 1;
  while (length(result) < paginatorWidth) {
    const leftPage = selectedPage - cursor;
    const rightPage = selectedPage + cursor;
    if (leftPage >= 1) result = [leftPage, ...result];
    if (rightPage <= maxPage) result = [...result, rightPage];
    cursor += 1;
  }
  return result;
};

export const paginationWithDots = ({ selectedPage, maxPage, paginatorWidth }) => {
  const result = pagination({ selectedPage, maxPage, paginatorWidth });
  if (last(result) !== maxPage) {
    result.pop();
    result.pop();
    result.push(null);
    result.push(maxPage);
  }
  if (head(result) !== 1) {
    result.shift();
    result.shift();
    result.unshift(null);
    result.unshift(1);
  }
  return result;
};

export default pagination;
