import { splitAt, join } from 'ramda';

export const moneyToString = (money) => {
  const [int, decimal] = money.toString().split('.');
  const groups = [];
  let tail = int;
  let head = null;
  while (tail) {
    [tail, head] = splitAt(-3, tail);
    groups.unshift(head);
  }
  const formattedInt = join('\u00A0', groups);
  return decimal ? `${formattedInt}.${decimal}` : formattedInt;
};
