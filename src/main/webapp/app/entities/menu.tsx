import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/card">
        <Translate contentKey="global.menu.entities.card" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/store">
        <Translate contentKey="global.menu.entities.store" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/perk">
        <Translate contentKey="global.menu.entities.perk" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
