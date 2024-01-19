import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICard } from 'app/shared/model/card.model';
import { getEntities as getCards } from 'app/entities/card/card.reducer';
import { IPerk } from 'app/shared/model/perk.model';
import { getEntity, updateEntity, createEntity, reset } from './perk.reducer';

export const PerkUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cards = useAppSelector(state => state.card.entities);
  const perkEntity = useAppSelector(state => state.perk.entity);
  const loading = useAppSelector(state => state.perk.loading);
  const updating = useAppSelector(state => state.perk.updating);
  const updateSuccess = useAppSelector(state => state.perk.updateSuccess);

  const handleClose = () => {
    navigate('/perk' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCards({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.expirationDate = convertDateTimeToServer(values.expirationDate);

    const entity = {
      ...perkEntity,
      ...values,
      card: cards.find(it => it.id.toString() === values.card.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          expirationDate: displayDefaultDateTime(),
        }
      : {
          ...perkEntity,
          expirationDate: convertDateTimeFromServer(perkEntity.expirationDate),
          card: perkEntity?.card?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cardperksApp.perk.home.createOrEditLabel" data-cy="PerkCreateUpdateHeading">
            <Translate contentKey="cardperksApp.perk.home.createOrEditLabel">Create or edit a Perk</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="perk-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('cardperksApp.perk.name')} id="perk-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('cardperksApp.perk.description')}
                id="perk-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('cardperksApp.perk.expirationDate')}
                id="perk-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('cardperksApp.perk.active')}
                id="perk-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('cardperksApp.perk.expired')}
                id="perk-expired"
                name="expired"
                data-cy="expired"
                check
                type="checkbox"
              />
              <ValidatedField id="perk-card" name="card" data-cy="card" label={translate('cardperksApp.perk.card')} type="select">
                <option value="" key="0" />
                {cards
                  ? cards.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/perk" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PerkUpdate;
