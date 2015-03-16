require 'fileutils'

dirs = %w(CoarseList FineGrainList Lazierlist LazyList)

dirs.each do |dir|
  FileUtils.cd dir do
    system 'make test'
  end
end
