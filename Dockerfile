FROM quay.io/keycloak/keycloak:25.0.2

ENV KEYCLOAK_HOME=/opt/keycloak
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_SPI_THEME_STATIC_MAX_AGE=-1
ENV KC_SPI_THEME_CACHE_THEMES=false
ENV KC_SPI_THEME_CACHE_TEMPLATES=false

ADD --chown=keycloak:keycloak --chmod=644 ./providers/*.jar ${KEYCLOAK_HOME}/providers/

COPY ./themes ${KEYCLOAK_HOME}/themes/

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
