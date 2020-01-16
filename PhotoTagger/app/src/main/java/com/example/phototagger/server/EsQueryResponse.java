package com.example.phototagger.server;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EsQueryResponse {
    @SerializedName("timed_out")
    private boolean timedOut;
    private Hits hits;

    public boolean getTimedOut() {
        return timedOut;
    }

    public Hits getHits() {
        return hits;
    }

    public class Hits {
        private List<InnerHits> hits;

        public List<InnerHits> getHits() {
            return hits;
        }
    }

    public class InnerHits {
        @SerializedName("_index")
        private String index;
        @SerializedName("_type")
        private String type;
        @SerializedName("_id")
        private String id;
        @SerializedName("_score")
        private float score;
        @SerializedName("_source")
        private Source source;

        public Source getSource() {
            return source;
        }
    }

    public class Source {
        @SerializedName("image_name")
        private String imageName;
        private List<String> labels;

        public String getImageName() {
            return imageName;
        }

        public List<String> getLabels() {
            return labels;
        }
    }
}
