original/coverdata.zip:
	wget -O original/coverdata.zip http://due.esrin.esa.int/files/Globcover2009_V2.3_Global_.zip

dl_data: original/coverdata.zip
	pushd original ; unzip coverdata.zip ; popd

all: dl_data
