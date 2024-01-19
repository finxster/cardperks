import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Perk from './perk';
import PerkDetail from './perk-detail';
import PerkUpdate from './perk-update';
import PerkDeleteDialog from './perk-delete-dialog';

const PerkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Perk />} />
    <Route path="new" element={<PerkUpdate />} />
    <Route path=":id">
      <Route index element={<PerkDetail />} />
      <Route path="edit" element={<PerkUpdate />} />
      <Route path="delete" element={<PerkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PerkRoutes;
