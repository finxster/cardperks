import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './perk.reducer';

export const PerkDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const perkEntity = useAppSelector(state => state.perk.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="perkDetailsHeading">
          <Translate contentKey="cardperksApp.perk.detail.title">Perk</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{perkEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cardperksApp.perk.name">Name</Translate>
            </span>
          </dt>
          <dd>{perkEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="cardperksApp.perk.description">Description</Translate>
            </span>
          </dt>
          <dd>{perkEntity.description}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="cardperksApp.perk.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {perkEntity.expirationDate ? <TextFormat value={perkEntity.expirationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="active">
              <Translate contentKey="cardperksApp.perk.active">Active</Translate>
            </span>
          </dt>
          <dd>{perkEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="expired">
              <Translate contentKey="cardperksApp.perk.expired">Expired</Translate>
            </span>
          </dt>
          <dd>{perkEntity.expired ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cardperksApp.perk.card">Card</Translate>
          </dt>
          <dd>{perkEntity.card ? perkEntity.card.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/perk" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/perk/${perkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PerkDetail;
