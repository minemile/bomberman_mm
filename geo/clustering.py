import random
import pandas as pd
import matplotlib.pyplot as plt
import geopandas as gpd
import plotly.express as px
from shapely.geometry import LineString, Point, Polygon
import networkx as nx
import osmnx as ox
import plotly.graph_objects as go
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler


class PathClusters(object):
    def __init__(self, n_clusters):
        self.n_clusters = n_clusters
        self.cluster_algo = KMeans(n_clusters=self.n_clusters)
        self.ss = StandardScaler()
        self.trip_id_to_cluster = None

    def fit(self, df):
        geo_df = self.trip_to_geo(df)
        trip_id_grouped = geo_df.groupby("trip_id")
        trip_id_features = []
        for group, rows in trip_id_grouped:
            features = self.extract_trip_features(rows)
            features['trip_id'] = group
            trip_id_features.append(features)
        trip_id_features = pd.DataFrame(trip_id_features)

        scaled_features = self.ss.fit_transform(trip_id_features.drop(["trip_id"], axis=1))
        clusters = self.cluster_algo.fit(scaled_features)

        self.trip_id_to_cluster = trip_id_features[['trip_id']].copy()
        self.trip_id_to_cluster['clusters'] = clusters
        return self
    
    def predict(self, df):
        geo_df = self.trip_to_geo(df)
        trip_id_grouped = geo_df.groupby("trip_id")
        trip_id_features = []
        for group, rows in trip_id_grouped:
            features = self.extract_trip_features(rows)
            features['trip_id'] = group
            trip_id_features.append(features)
        trip_id_features = pd.DataFrame(trip_id_features)
        scaled_features = self.ss.transform(trip_id_features.drop(['trip_id'], axis=1))

        trip_id_to_cluster = trip_id_features[['trip_id']].copy()
        trip_id_to_cluster['clusters'] = self.cluster_algo.predict(scaled_features)
        return trip_id_to_cluster

    @staticmethod
    def trip_to_geo(df):
        geo_m = df.copy()
        geo_m['geometry'] = geo_m.apply(lambda x: Point(x['lon'], x['lat']), axis=1)
        geo_m = gpd.GeoDataFrame(geo_m, crs='+init=epsg:4326')
        return geo_m
    
    @staticmethod
    def extract_trip_features(trip_rows):
        total_bounds = trip_rows['geometry'].total_bounds
        bounds = {}
        bounds['minx'] = total_bounds[0]
        bounds['miny'] = total_bounds[1]
        bounds['maxx'] = total_bounds[2]
        bounds['maxy'] = total_bounds[3]
        return bounds
