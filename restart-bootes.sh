#!/bin/sh
sudo systemctl daemon-reload
sudo systemctl enable bootes.service
sudo systemctl start bootes
sudo systemctl status bootes
