import React, { memo, Suspense } from "react";
import { BrowserRouter } from "react-router-dom";
import { renderRoutes } from "react-router-config";
import { Provider } from "react-redux";

import AppHeader from "@/components/app-header";
import AppFooter from "@/components/app-footer";
import routes from "@/router";
import store from "@/store";

function App() {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <AppHeader />
        <Suspense fallback={<div> page loading... </div>}>
          {renderRoutes(routes)}
        </Suspense>
        <AppFooter />
      </BrowserRouter>
    </Provider>
  );
}

export default memo(App);
