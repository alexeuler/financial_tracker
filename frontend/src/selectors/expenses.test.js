import { getReport } from './expenses';

const testCases = [
// 2016 - 2017
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2016-12-24 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-12-25 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-12-26 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-12-27 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-12-28 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-12-29 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-12-30 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-12-31 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-01 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-02 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-03 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-04 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-05 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-06 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-07 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-08 00:00',
          },
        ],
      },
    },
  },

// 2015 - 2016
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2015-12-26 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-12-27 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-12-28 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-12-29 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-12-30 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-12-31 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-01 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-02 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-03 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-04 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-05 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-06 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-07 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-08 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-09 00:00',
          },
          {
            amount: 1,
            occuredAt: '2016-01-10 00:00',
          },
        ],
      },
    },
  },


// 2014 - 2015
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2014-12-27 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-12-28 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-12-29 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-12-30 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-12-31 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-01 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-02 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-03 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-04 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-05 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-06 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-07 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-08 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-09 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-10 00:00',
          },
          {
            amount: 1,
            occuredAt: '2015-01-11 00:00',
          },

        ],
      },
    },
  },


// 2013 - 2014
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2013-12-28 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-12-29 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-12-30 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-12-31 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-01 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-02 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-03 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-04 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-05 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-06 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-07 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-08 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-09 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-10 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-11 00:00',
          },
          {
            amount: 1,
            occuredAt: '2014-01-12 00:00',
          },
        ],
      },
    },
  },

// 2012 - 2013
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2012-12-29 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-12-30 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-12-31 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-01 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-02 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-03 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-04 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-05 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-06 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-07 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-08 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-09 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-10 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-11 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-12 00:00',
          },
          {
            amount: 1,
            occuredAt: '2013-01-13 00:00',
          },
        ],
      },
    },
  },

// 2011 - 2012
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2011-12-24 00:00',
          },
          {
            amount: 1,
            occuredAt: '2011-12-25 00:00',
          },
          {
            amount: 1,
            occuredAt: '2011-12-26 00:00',
          },
          {
            amount: 1,
            occuredAt: '2011-12-27 00:00',
          },
          {
            amount: 1,
            occuredAt: '2011-12-28 00:00',
          },
          {
            amount: 1,
            occuredAt: '2011-12-29 00:00',
          },
          {
            amount: 1,
            occuredAt: '2011-12-30 00:00',
          },
          {
            amount: 1,
            occuredAt: '2011-12-31 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-01 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-02 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-03 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-04 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-05 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-06 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-07 00:00',
          },
          {
            amount: 1,
            occuredAt: '2012-01-08 00:00',
          },
        ],
      },
    },
  },

// sparse
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2016-12-24 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-08 00:00',
          },
        ],
      },
    },
  },

// edge
  {
    expensesStore: {
      entities: {
        1: [
          {
            amount: 1,
            occuredAt: '2017-01-07 23:59',
          },
          {
            amount: 1,
            occuredAt: '2017-01-08 00:00',
          },
          {
            amount: 1,
            occuredAt: '2017-01-08 00:01',
          },
        ],
      },
    },
  },

];

const results = [
  {
    entries: [
      {
        avg: 1 / 7,
        start: '2016-12-18',
        end: '2016-12-24',
        sum: 1,
      },
      {
        avg: 1,
        start: '2016-12-25',
        end: '2016-12-31',
        sum: 7,
      },
      {
        avg: 1,
        start: '2017-01-01',
        end: '2017-01-07',
        sum: 7,
      },
      {
        avg: 1 / 7,
        start: '2017-01-08',
        end: '2017-01-14',
        sum: 1,
      },
    ],
    avg: 1,
    sum: 16,
  },
  {
    entries: [
      {
        avg: 1 / 7,
        start: '2015-12-20',
        end: '2015-12-26',
        sum: 1,
      },
      {
        avg: 1,
        start: '2015-12-27',
        end: '2016-01-02',
        sum: 7,
      },
      {
        avg: 1,
        start: '2016-01-03',
        end: '2016-01-09',
        sum: 7,
      },
      {
        avg: 1 / 7,
        start: '2016-01-10',
        end: '2016-01-16',
        sum: 1,
      },
    ],
    avg: 1,
    sum: 16,
  },
  {
    entries: [
      {
        avg: 1 / 7,
        start: '2014-12-21',
        end: '2014-12-27',
        sum: 1,
      },
      {
        avg: 1,
        start: '2014-12-28',
        end: '2015-01-03',
        sum: 7,
      },
      {
        avg: 1,
        start: '2015-01-04',
        end: '2015-01-10',
        sum: 7,
      },
      {
        avg: 1 / 7,
        start: '2015-01-11',
        end: '2015-01-17',
        sum: 1,
      },
    ],
    avg: 1,
    sum: 16,
  },
  {
    entries: [
      {
        avg: 1 / 7,
        start: '2013-12-22',
        end: '2013-12-28',
        sum: 1,
      },
      {
        avg: 1,
        start: '2013-12-29',
        end: '2014-01-04',
        sum: 7,
      },
      {
        avg: 1,
        start: '2014-01-05',
        end: '2014-01-11',
        sum: 7,
      },
      {
        avg: 1 / 7,
        start: '2014-01-12',
        end: '2014-01-18',
        sum: 1,
      },
    ],
    avg: 1,
    sum: 16,
  },

  {
    entries: [
      {
        avg: 1 / 7,
        start: '2012-12-23',
        end: '2012-12-29',
        sum: 1,
      },
      {
        avg: 1,
        start: '2012-12-30',
        end: '2013-01-05',
        sum: 7,
      },
      {
        avg: 1,
        start: '2013-01-06',
        end: '2013-01-12',
        sum: 7,
      },
      {
        avg: 1 / 7,
        start: '2013-01-13',
        end: '2013-01-19',
        sum: 1,
      },
    ],
    avg: 1,
    sum: 16,
  },

  {
    entries: [
      {
        avg: 1 / 7,
        start: '2011-12-18',
        end: '2011-12-24',
        sum: 1,
      },
      {
        avg: 1,
        start: '2011-12-25',
        end: '2011-12-31',
        sum: 7,
      },
      {
        avg: 1,
        start: '2012-01-01',
        end: '2012-01-07',
        sum: 7,
      },
      {
        avg: 1 / 7,
        start: '2012-01-08',
        end: '2012-01-14',
        sum: 1,
      },
    ],
    avg: 1,
    sum: 16,
  },

// sparse
  {
    entries: [
      {
        avg: 1 / 7,
        start: '2016-12-18',
        end: '2016-12-24',
        sum: 1,
      },
      {
        avg: 0,
        start: '2016-12-25',
        end: '2016-12-31',
        sum: 0,
      },
      {
        avg: 0,
        start: '2017-01-01',
        end: '2017-01-07',
        sum: 0,
      },
      {
        avg: 1 / 7,
        start: '2017-01-08',
        end: '2017-01-14',
        sum: 1,
      },
    ],
    avg: 2 / 16,
    sum: 2,
  },

// edge
  {
    entries: [
      {
        avg: 1 / 7,
        start: '2017-01-01',
        end: '2017-01-07',
        sum: 1,
      },
      {
        avg: 2 / 7,
        start: '2017-01-08',
        end: '2017-01-14',
        sum: 2,
      },
    ],
    avg: 3 / 2,
    sum: 3,
  },

];



test('2016 - 2017', () => {
  expect(getReport(testCases[0], 1)).toEqual(results[0]);
});

test('2015 - 2016', () => {
  expect(getReport(testCases[1], 1)).toEqual(results[1]);
});

test('2014 - 2015', () => {
  expect(getReport(testCases[2], 1)).toEqual(results[2]);
});

test('2013 - 2014', () => {
  expect(getReport(testCases[3], 1)).toEqual(results[3]);
});

test('2012 - 2013', () => {
  expect(getReport(testCases[4], 1)).toEqual(results[4]);
});

test('2011 - 2012', () => {
  expect(getReport(testCases[5], 1)).toEqual(results[5]);
});

test('sparse data', () => {
  expect(getReport(testCases[6], 1)).toEqual(results[6]);
});

test('edge data', () => {
  expect(getReport(testCases[7], 1)).toEqual(results[7]);
});

