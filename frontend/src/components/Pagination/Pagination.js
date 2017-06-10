import React from 'react';
import PropTypes from 'prop-types';
import { paginationWithDots } from './utils';
import PageButton from './PageButton';

const Pagination = (props) => {
  if (props.maxPage < 2) return null;
  return (
    <div className="flex justify-center pa3">
      <div className="flex">
        <PageButton
          disabled={props.selectedPage === 1}
          onClick={() => props.onPageSelected(props.selectedPage - 1)}
        >
          {'<'}
        </PageButton>
        {paginationWithDots(props).map((page, index) =>
          <PageButton
            key={index}
            disabled={!page}
            active={props.selectedPage === page}
            onClick={() => props.onPageSelected(page)}
          >
            <div>{page || '...'}</div>
          </PageButton>,
        )}
        <PageButton
          disabled={props.selectedPage === props.maxPage}
          onClick={() => props.onPageSelected(props.selectedPage + 1)}
        >
          {'>'}
        </PageButton>
      </div>
    </div>
  );
};

Pagination.propTypes = {
  selectedPage: PropTypes.number.isRequired,
  maxPage: PropTypes.number.isRequired,
  // eslint-disable-next-line
  paginatorWidth: PropTypes.number.isRequired,
  onPageSelected: PropTypes.func.isRequired,
};

export default Pagination;
