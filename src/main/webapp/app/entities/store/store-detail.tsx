import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './store.reducer';

export const StoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storeEntity = useAppSelector(state => state.store.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storeDetailsHeading">
          <Translate contentKey="cardperksApp.store.detail.title">Store</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cardperksApp.store.name">Name</Translate>
            </span>
          </dt>
          <dd>{storeEntity.name}</dd>
          <dt>
            <Translate contentKey="cardperksApp.store.perk">Perk</Translate>
          </dt>
          <dd>{storeEntity.perk ? storeEntity.perk.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/store" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/store/${storeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StoreDetail;
